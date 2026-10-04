import { HttpHeaders } from '@angular/common/http';
import { Component, effect, inject, signal, untracked } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, Data, ParamMap, Router, RouterLink } from '@angular/router';

import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap/modal';
import { NgbPagination } from '@ng-bootstrap/ng-bootstrap/pagination';
import { combineLatest, filter, map, tap } from 'rxjs';

import { DEFAULT_SORT_DATA, ITEMS_PER_PAGE, ITEM_DELETED_EVENT, PAGE_HEADER, SORT, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config';
import { Alert, AlertError } from 'app/shared/alert';
import { ItemCount } from 'app/shared/pagination';
import { SortByDirective, SortDirective, SortService, type SortState, sortStateSignal } from 'app/shared/sort';
import { SupplierDeleteDialog } from '../delete/supplier-delete-dialog';
import { SupplierService } from '../service/supplier.service';
import { ISupplier } from '../supplier.model';

@Component({
  selector: 'mi-supplier',
  templateUrl: './supplier.html',
  imports: [RouterLink, FontAwesomeModule, AlertError, Alert, SortDirective, SortByDirective, NgbPagination, ItemCount],
})
export class Supplier {
  readonly suppliers = signal<ISupplier[]>([]);

  sortState = sortStateSignal({});

  readonly itemsPerPage = signal(ITEMS_PER_PAGE);
  readonly totalItems = signal(0);
  readonly page = signal(1);

  readonly router = inject(Router);
  protected readonly supplierService = inject(SupplierService);
  // eslint-disable-next-line @typescript-eslint/member-ordering
  readonly isLoading = this.supplierService.suppliersResource.isLoading;
  protected readonly activatedRoute = inject(ActivatedRoute);
  protected readonly activatedRouteState = toSignal(
    combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      map(([queryParamMap, data]) => ({ queryParamMap, data })),
    ),
    { initialValue: { queryParamMap: this.activatedRoute.snapshot.queryParamMap, data: this.activatedRoute.snapshot.data } },
  );
  protected readonly sortService = inject(SortService);
  protected modalService = inject(NgbModal);

  constructor() {
    effect(() => {
      const headers = this.supplierService.suppliersResource.headers();
      if (headers) {
        this.fillComponentAttributesFromResponseHeader(headers);
      }
    });
    effect(() => {
      this.suppliers.set(this.fillComponentAttributesFromResponseBody([...this.supplierService.suppliers()]));
    });
    effect(() => {
      const activatedRouteState = this.activatedRouteState();
      untracked(() => {
        // Only watch for route changes. Other signals should be ignored.
        this.fillComponentAttributeFromRoute(activatedRouteState.queryParamMap, activatedRouteState.data);
        this.load();
      });
    });
  }

  trackId = (item: ISupplier): number => this.supplierService.getSupplierIdentifier(item);

  delete(supplier: ISupplier): void {
    const modalRef = this.modalService.open(SupplierDeleteDialog, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.supplier = supplier;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        tap(() => this.load()),
      )
      .subscribe();
  }

  load(): void {
    this.queryBackend();
  }

  navigateToWithComponentValues(event: SortState): void {
    this.handleNavigation(this.page(), event);
  }

  navigateToPage(page: number): void {
    this.handleNavigation(page, this.sortState());
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const page = params.get(PAGE_HEADER);
    this.page.set(+(page ?? 1));
    this.sortState.set(this.sortService.parseSortParam(params.get(SORT) ?? data[DEFAULT_SORT_DATA]));
  }

  protected fillComponentAttributesFromResponseBody(data: ISupplier[]): ISupplier[] {
    return data;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems.set(Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER)));
  }

  protected queryBackend(): void {
    const pageToLoad: number = this.page();
    const queryObject: any = {
      page: pageToLoad - 1,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(this.sortState()),
    };
    this.supplierService.suppliersParams.set(queryObject);
  }

  protected handleNavigation(page: number, sortState: SortState): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage(),
      sort: this.sortService.buildSortParam(sortState),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
}
