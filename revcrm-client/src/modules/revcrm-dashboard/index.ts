
import { UIManager } from "../../UIManager"
import { Dashboard } from "./components/Dashboard"

export function register(ui: UIManager) {

    ui.registerPerspective({
        id: "dashboard",
        title: "Dashboard",
        model: "Dashboard",
        views: {
            my: {
                title: "My Dashboard",
                viewId: "my_dashboard",
            },
        },
        defaultView: "my"
    })

    ui.registerView({
        id: "my_dashboard",
        model: null,
        component: Dashboard
    })

    ui.registerMenu({
        id: "menu_dashboard",
        icon: "insert_chart",
        label: "Dashboard",
        subItems: [
            { label: "My Dashboard", perspective: "dashboard", viewName: "my" },
            { label: "Team Dashboard", perspective: "dashboard", viewName: "team" },
        ]
    })

}
