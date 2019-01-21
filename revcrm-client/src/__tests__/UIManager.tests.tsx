import * as React from "react"
import { IPerspective, IView, UIManager } from "../UIManager"

const mockPerspective: IPerspective = {
    id: "dashboard",
    title: "Dashboard",
    views: {
        my: {
            title: "My Dashboard",
            viewId: "dashboard",
        },
    }
}

const mockMyView: IView = {
    id: "dashboard",
    model: null,
    component: () => <div id="mockMyDashboard" />
}

describe("UIManager", () => {

    it("I can register a perspective and associated view and read them back", () => {
        const ui = new UIManager()
        ui.registerPerspective(mockPerspective)
        ui.registerView(mockMyView)

        const perspective = ui.getPerspective(mockPerspective.id)!
        const view = perspective.views["my"]

        expect(perspective).toEqual(mockPerspective)
        expect(view).toEqual(mockPerspective.views["my"])
    })

    it("getPerspective returns null if perspective does not exist", () => {
        const ui = new UIManager()
        ui.registerPerspective(mockPerspective)
        ui.registerView(mockMyView)

        expect(ui.getPerspective("no_existy")).toBeNull()
    })

    it("getView returns null if view does not exist", () => {
        const ui = new UIManager()
        ui.registerPerspective(mockPerspective)
        ui.registerView(mockMyView)

        expect(ui.getView("no_existy")).toBeNull()
    })

})
