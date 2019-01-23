
import { UIManager } from "../../UIManager"
import React from "react"
import { SearchView } from "../../components/views/SearchView"
import { FormView } from "../../components/views/FormView"
import { Panel } from "../../components/views/widgets/Panel"
import { Field } from "../../components/views/fields/Field"
import { Tabs } from "../../components/views/widgets/Tabs"

export function registerCompanyViews(ui: UIManager) {

    ui.registerView({
        id: "accounts_company_list",
        model: "account",
        component: () =>
            <SearchView
                entity="Account"
                title="Companies"
                showFields={[
                    "company_name",
                    "tags",
                    "email",
                    "phone",
                    "website",
                    // primary contact
                ]}
                detailView="companies/form"
            />
    })

    ui.registerView({
        id: "accounts_company_form",
        model: "account",
        component: () => (
            <FormView entity="Account">
                <Panel title="Company Summary" colspan={12}>
                    <Field name="company_name" colspan={9} />
                    <Field name="is_company" colspan={3} />
                    <Field name="code" colspan={2} />
                    <Field name="tags" colspan={5} />
                    <Field name="source" colspan={5} />
                </Panel>
                <Tabs
                    colspan={12}
                    tabs={[
                        {
                            label: "Contact Details",
                            component: () => (<>
                                <Field name="title" colspan={2} />
                                <Field name="first_name" colspan={4} />
                                <Field name="last_name" colspan={6} />
                                <Field name="phone" />
                                <Field name="email" />
                                <Field name="mobile" />
                                <Field name="website" />
                            </>)
                        },
                        {
                            label: "Notes",
                            component: () => (
                                <Field name="notes" colspan={12} />
                            )
                        }
                    ]} />
            </FormView>
        )
    })
}
