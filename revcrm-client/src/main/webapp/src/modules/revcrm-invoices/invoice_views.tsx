
import { UIManager } from "../../UIManager"
import React from "react"
import { SearchView } from "../../components/views/SearchView"
import { FormView } from "../../components/views/FormView"
import { Panel } from "../../components/views/widgets/Panel"
import { Field } from "../../components/views/fields/Field"
import { Tabs } from "../../components/views/widgets/Tabs"
import { InvoicePrint } from "./invoice_print_view"

export function registerInvoiceViews(ui: UIManager) {

    ui.registerView({
        id: "invoices_list",
        entity: "Invoice",
        component: () =>
            <SearchView
                entity="Invoice"
                title="Invoices"
                showFields={[
                    "account",
                    "invoice_number",
                    "invoice_date",
                    "invoice_total",
                    "invoice_currency",
                    "payment_due_date",
                ]}
                detailView="invoices/form"
            />
    })

    ui.registerView({
        id: "invoices_form",
        entity: "Invoice",
        component: () => (
            <FormView entity="Invoice">
                <Panel title="Invoice Summary" colspan={12}>
                    <Field name="account" />
                    <Field name="invoice_number" />
                    <Field name="invoice_date" />
                    <Field name="invoice_currency" />
                    <Field name="payment_due_date" />
                </Panel>
                <Tabs
                    colspan={12}
                    tabs={[
                        {
                            label: "Items",
                            component: () => (<>
                                <Field name="lines" colspan={12}>
                                    <Field name="item" colspan={12} />
                                    <Field name="quantity" colspan={12} />
                                    <Field name="unit" colspan={12} />
                                    <Field name="unit_price" colspan={12} />
                                    <Field name="discount_amount" colspan={12} />
                                    <Field name="net_total" colspan={12} />
                                    <Field name="line_tax" colspan={12} />
                                    <Field name="line_total" colspan={12} />
                                </Field>
                                <Field name="invoice_discount_amount" />
                                <Field name="invoice_net_total" />
                                <Field name="invoice_tax_total" />
                                <Field name="invoice_total" />
                            </>)
                        },
                    ]} />
            </FormView>
        )
    })

    ui.registerView({
        id: "invoices_print",
        entity: "Invoice",
        component: InvoicePrint
    })
}
