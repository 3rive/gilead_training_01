import { HttpClient, HttpResponse, httpResource } from '@angular/common/http';
import { Service, computed, inject, signal } from '@angular/core';

import { Observable } from 'rxjs';

import { serverApiUrl } from 'app/config';
import { createRequestOption } from 'app/core/request';
import { ICategory, NewCategory } from '../category.model';

export type PartialUpdateCategory = Partial<ICategory> & Pick<ICategory, 'id'>;

@Service()
export class CategoriesService {
  readonly categoriesParams = signal<Record<string, string | number | boolean | readonly (string | number | boolean)[]> | undefined>(
    undefined,
  );
  readonly categoriesResource = httpResource<ICategory[]>(() => {
    const params = this.categoriesParams();
    if (!params) {
      return undefined;
    }
    return { url: this.resourceUrl, params };
  });
  /**
   * This signal holds the list of category that have been fetched. It is updated when the categoriesResource emits a new value.
   * In case of error while fetching the categories, the signal is set to an empty array.
   */
  readonly categories = computed(() => (this.categoriesResource.hasValue() ? this.categoriesResource.value() : []));
  protected readonly resourceUrl = `${serverApiUrl}api/categories`;
}

@Service()
export class CategoryService extends CategoriesService {
  protected readonly http = inject(HttpClient);

  create(category: NewCategory): Observable<ICategory> {
    return this.http.post<ICategory>(this.resourceUrl, category);
  }

  update(category: ICategory): Observable<ICategory> {
    return this.http.put<ICategory>(`${this.resourceUrl}/${encodeURIComponent(this.getCategoryIdentifier(category))}`, category);
  }

  partialUpdate(category: PartialUpdateCategory): Observable<ICategory> {
    return this.http.patch<ICategory>(`${this.resourceUrl}/${encodeURIComponent(this.getCategoryIdentifier(category))}`, category);
  }

  find(id: number): Observable<ICategory> {
    return this.http.get<ICategory>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  query(req?: any): Observable<HttpResponse<ICategory[]>> {
    const options = createRequestOption(req);
    return this.http.get<ICategory[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<undefined> {
    return this.http.delete<undefined>(`${this.resourceUrl}/${encodeURIComponent(id)}`);
  }

  getCategoryIdentifier(category: Pick<ICategory, 'id'>): number {
    return category.id;
  }

  compareCategory(o1: Pick<ICategory, 'id'> | null, o2: Pick<ICategory, 'id'> | null): boolean {
    return o1 && o2 ? this.getCategoryIdentifier(o1) === this.getCategoryIdentifier(o2) : o1 === o2;
  }

  addCategoryToCollectionIfMissing<Type extends Pick<ICategory, 'id'>>(
    categoryCollection: Type[],
    ...categoriesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const categories: Type[] = categoriesToCheck.filter(categoryItem => categoryItem !== null && categoryItem !== undefined);
    if (categories.length > 0) {
      const categoryCollectionIdentifiers = categoryCollection.map(categoryItem => this.getCategoryIdentifier(categoryItem));
      const categoriesToAdd = categories.filter(categoryItem => {
        const categoryIdentifier = this.getCategoryIdentifier(categoryItem);
        if (categoryCollectionIdentifiers.includes(categoryIdentifier)) {
          return false;
        }
        categoryCollectionIdentifiers.push(categoryIdentifier);
        return true;
      });
      return [...categoriesToAdd, ...categoryCollection];
    }
    return categoryCollection;
  }
}
