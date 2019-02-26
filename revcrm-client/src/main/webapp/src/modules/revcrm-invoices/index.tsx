
import { UIManager } from "../../UIManager"
import { registerInvoiceViews } from "./invoice_views"

export function register(ui: UIManager) {

    registerInvoiceViews(ui)

    ui.registerPerspective({
        id: "invoices",
        title: "Invoices",
        entity: "Invoices",
        views: {
            list: { viewId: "invoices_list", title: "Invoices" },
            form: {
                viewId: "invoices_form",
                title: "Invoice",
                actions: [
                    {
                        label: "Print",
                        icon: "print",
                        type: "open_view",
                        perspective: "invoices",
                        viewName: "print"
                    }
                ]
            },
            print: { viewId: "invoices_print", title: "Print Invoice" }
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
