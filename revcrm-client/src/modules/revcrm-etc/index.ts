
import { UIManager } from "../../UIManager"

export function register(ui: UIManager) {

    ui.registerMenu({
        id: "menu_calendar",
        label: "Calendar",
        icon: "insert_invitation",
        subItems: [
            { label: "My Calendar", perspective: "calendar", viewName: "my" },
            { label: "Team Calendar", perspective: "calendar", viewName: "team" },
        ]
    })
    ui.registerMenu({
        id: "menu_opportunities",
        label: "Sales & Marketing",
        icon: "monetization_on_sharp",
        subItems: [
            { label: "Sales Opportunities", perspective: "opportunities", viewName: "list" },
        ]
    })
    ui.registerMenu({
        id: "menu_cases",
        label: "Customer Service",
        icon: "assignment",
        subItems: [
            { label: "My Cases", perspective: "cases", viewName: "my" },
            { label: "Team Cases", perspective: "cases", viewName: "team" },
        ]
    })

}
