
import { UIManager } from "../../UIManager"
import { AccountList } from "./components/AccountList"
import { AccountForm } from "./components/AccountForm"

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
        component: AccountList
    })

    ui.registerView({
        id: "accounts_form",
        model: "account",
        component: AccountForm
    })

    ui.registerMenu({
        id: "menu_accounts",
        label: "Companies & Contacts",
        perspective: "accounts",
        icon: "supervised_user_circle",
        subItems: [
            { label: "Companies", perspective: "accounts", view: "companies" },
            { label: "Contacts", perspective: "accounts", view: "contacts" },
            { label: "Leads", perspective: "accounts", view: "leads" },
            { label: "Data Import", perspective: "accounts", view: "form" },
        ]
    })

}
