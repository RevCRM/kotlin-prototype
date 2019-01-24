
import { UIManager } from "../../UIManager"
import { registerInvoiceViews } from "./invoice_views"

export function register(ui: UIManager) {

    registerInvoiceViews(ui)

    ui.registerPerspective({
        id: "invoices",
        title: "Invoices",
        model: "Invoices",
        where: { is_company: true },
        views: {
            list: { viewId: "invoices_list", title: "Invoices" },
            form: { viewId: "invoices_form", title: "Invoice" },
        },
        defaultView: "list"
    })

    ui.registerMenu({
        id: "menu_sales",
        label: "Sales & Marketing",
        icon: "monetization_on_sharp",
        subItems: [
            { label: "Sales Opportunities", perspective: "opportunities", viewName: "list" },
            { label: "Invoices", perspective: "invoices", viewName: "list" },
        ]
    })

}
