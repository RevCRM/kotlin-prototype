
import { UIManager } from "../../UIManager"
import React from "react"
import { SearchView } from "../../components/views/SearchView"
import { FormView } from "../../components/views/FormView"
import { Panel } from "../../components/views/widgets/Panel"
import { Field } from "../../components/views/fields/Field"
import { Tabs } from "../../components/views/widgets/Tabs"

export function registerInvoiceViews(ui: UIManager) {

    ui.registerView({
        id: "invoices_list",
        model: "Invoice",
        component: () =>
            <SearchView
                entity="Invoice"
                title="Invoices"
                showFields={[
                    "invoice_number",
                    "invoice_date",
                    "invoice_total",
                    "invoice_currency",
                ]}
                detailView="invoices/form"
            />
    })

    ui.registerView({
        id: "invoices_form",
        model: "Invoice",
        component: () => (
            <FormView entity="Invoice">
                <Panel title="Invoice Summary" colspan={12}>
                    <Field name="invoice_number" />
                    <Field name="invoice_date" />
                    <Field name="invoice_currency" />
                </Panel>
                <Tabs
                    colspan={12}
                    tabs={[
                        {
                            label: "Items",
                            component: () => (<>
                                <Field name="invoice_total" />
                                <Field name="invoice_net_total" />
                                <Field name="invoice_tax_total" />
                                <Field name="discount_amount" />
                            </>)
                        },
                    ]} />
            </FormView>
        )
    })
}
