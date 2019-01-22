
import { UIManager } from "../../UIManager"
import { registerCompanyViews } from "./company_views"
import { registerContactViews } from "./contact_views"

export function register(ui: UIManager) {

    registerCompanyViews(ui)
    registerContactViews(ui)

    ui.registerPerspective({
        id: "companies",
        title: "Companies",
        views: {
            list: { viewId: "accounts_company_list", title: "Companies" },
            form: { viewId: "accounts_company_form", title: "Company" },
        },
        defaultView: "list"
    })

    ui.registerPerspective({
        id: "contacts",
        title: "Contacts",
        views: {
            list: { viewId: "accounts_contact_list", title: "Contacts" },
            form: { viewId: "accounts_contact_form", title: "Contact" },
        },
        defaultView: "list"
    })

    ui.registerMenu({
        id: "menu_accounts",
        label: "Companies & Contacts",
        icon: "supervised_user_circle",
        subItems: [
            { label: "Companies", perspective: "companies" },
            { label: "Contacts", perspective: "contacts" },
            { label: "Leads", perspective: "leads" },
            { label: "Data Import", perspective: "account_import" },
        ]
    })

}
