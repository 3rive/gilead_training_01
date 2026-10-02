# Homeward

A frontend for an NRI loan desk. Indians living in the Gulf, the US, the UK, Singapore, and elsewhere can compare illustrative Indian loan products, raise one request, and track that file.

The shape follows a working aggregator such as [Loans Got Easy](https://loansgoteasy.com/): several loan types, a lender rate card, an EMI sketch, a short application, and a file that moves from request to disbursement. Homeward is aimed at non-residents, so the form asks for the country of residence, overseas income, an NRE or NRO account, a resident co-applicant, and whether a power of attorney is needed.

Nothing here calls a bank. Rates, lenders, and sample files are mock data in the browser. Choosing an offer only updates that local file.

## Run

```bash
cd nri-loan-desk
npm install
npm run dev
```

Open the printed local URL.

Sample references already on the track page: `HW-24018`, `HW-23880`, `HW-24102`.

```bash
npm run build
```
