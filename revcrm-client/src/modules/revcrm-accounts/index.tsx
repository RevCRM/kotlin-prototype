
import { UIManager } from "../../UIManager"
import { AccountForm } from "./components/AccountForm"
import React from "react"
import { SearchView } from "../../components/views/SearchView"

export function register(ui: UIManager) {

    ui.registerPerspective({
        id: "accounts",
        title: "Accounts",
        views: {
            companies: {
                title: "Companies",
                viewId: "accounts_company_list",
            },
            contacts: {
                title: "Contacts",
                viewId: "accounts_contact_list",
            },
            form: {
                title: "Account",
                viewId: "accounts_form",
            },
        }
    })

    ui.registerView({
        id: "accounts_company_list",
        model: "account",
        component: () =>
            <SearchView
                entity="Account"
                title="Companies &amp; Contacts"
                showFields={[
                    "org_name",
                    "first_name",
                    "last_name",
                    "email",
                    "phone"
                ]}
                detailView="accounts/form"
            />
    })

    ui.registerView({
        id: "accounts_form",
        model: "account",
        component: AccountForm
    })

    ui.registerMenu({
        id: "menu_accounts",
        label: "Companies & Contacts",
        icon: "supervised_user_circle",
        subItems: [
            { label: "Companies", perspective: "accounts", viewName: "companies" },
            { label: "Contacts", perspective: "accounts", viewName: "contacts" },
            { label: "Leads", perspective: "accounts", viewName: "leads" },
            { label: "Data Import", perspective: "accounts", viewName: "form" },
        ]
    })

}
