
import * as React from "react"
import * as TestRenderer from "react-test-renderer"
import { ViewManager, IViewManagerContext, withViewManagerContext, IViewManagerContextProp } from "../ViewManager"
import { createMemoryHistory } from "history"
import { UI, IPerspective, IView } from "../../../UIManager"
import { sleep } from "../../../utils/sleep"

describe("<ViewManager />", () => {
    // let renderer: TestRenderer.ReactTestRenderer;
    // let instance: TestRenderer.ReactTestInstance;
    let receivedCtx: IViewManagerContext

    const ViewContextSpy = withViewManagerContext((props: IViewManagerContextProp) => {
        receivedCtx = props.view
        return <div />
    })

    const mockPerspective: IPerspective = {
        id: "dashboard",
        title: "Dashboard",
        entity: "Dashboard",
        views: {
            my: {
                title: "My Dashboard",
                viewId: "dashboard",
            },
            team: {
                title: "Team Dashboard",
                viewId: "dashboard_team",
            }
        },
        defaultView: "my"
    }
    UI.registerPerspective(mockPerspective)

    const mockMyView: IView = {
        id: "dashboard",
        entity: null,
        component: () => <div id="mockMyDashboard" />
    }
    UI.registerView(mockMyView)

    const mockTeamView: IView = {
        id: "dashboard_team",
        entity: null,
        component: () => <div id="mockTeamDashboard" />
    }
    UI.registerView(mockTeamView)

    describe("initialisation - when a matching perspective and view is set", () => {
        const expectedUrl = "/dashboard/team"

        beforeAll(() => {
            receivedCtx = null as any
            const history = createMemoryHistory({
                initialEntries: ["/dashboard/team"]
            })
            TestRenderer.create(
                <ViewManager history={history}>
                    <ViewContextSpy />
                </ViewManager>
            )
        })

        it("stays at expected url", () => {
            expect(receivedCtx.history.location.pathname).toEqual(expectedUrl)
        })

        it("sets correct perspective and view in the context", () => {
            expect(receivedCtx.perspective).toEqual(mockPerspective)
            expect(receivedCtx.viewName).toEqual("team")
            expect(receivedCtx.view).toEqual(mockTeamView)
        })

    })

    describe("initialisation - when just a matching perspective is set", () => {

        beforeAll(() => {
            receivedCtx = null as any
            const history = createMemoryHistory({
                initialEntries: ["/dashboard"]
            })
            TestRenderer.create(
                <ViewManager history={history}>
                    <ViewContextSpy />
                </ViewManager>
            )
        })

        it("stays at expected url", () => {
            expect(receivedCtx.history.location.pathname).toEqual("/dashboard")
        })

        it("sets correct perspective and view in the context", () => {
            expect(receivedCtx.perspective).toEqual(mockPerspective)
            expect(receivedCtx.viewName).toEqual("my")
            expect(receivedCtx.view).toEqual(mockMyView)
        })

    })

    describe("initialisation - when a non-matching URL is set", () => {
        const nonMatchingUrl = "/no_perspective"

        beforeAll(() => {
            receivedCtx = null as any
            const history = createMemoryHistory({
                initialEntries: [nonMatchingUrl]
            })
            TestRenderer.create(
                <ViewManager history={history}>
                    <ViewContextSpy />
                </ViewManager>
            )
        })

        it("stays at expected url", () => {
            expect(receivedCtx.history.location.pathname).toEqual(nonMatchingUrl)
        })

        it("perspective and view context is null", () => {
            expect(receivedCtx.perspective).toBeNull()
            expect(receivedCtx.viewName).toBeNull()
            expect(receivedCtx.view).toBeNull()
        })

    })

    describe("when channging perspective", () => {
        const firstPerspectiveUrl = "/dashboard/team"
        const secondPerspectiveUrl = "/dashboard/my"

        beforeAll(() => {
            receivedCtx = null as any
            const history = createMemoryHistory({
                initialEntries: [firstPerspectiveUrl]
            })
            TestRenderer.create(
                <ViewManager history={history}>
                    <ViewContextSpy />
                </ViewManager>
            )
        })

        it("starts at expected URL", () => {
            expect(receivedCtx.history.location.pathname).toEqual(firstPerspectiveUrl)
        })

        it("calling changePerspective redirects to the new URL", async () => {
            receivedCtx.changePerspective("dashboard", "my")
            await sleep(10)
            expect(receivedCtx.history.location.pathname).toEqual(secondPerspectiveUrl)
        })

        it("sets correct perspective and view in the context", () => {
            expect(receivedCtx.perspective).toEqual(mockPerspective)
            expect(receivedCtx.viewName).toEqual("my")
            expect(receivedCtx.view).toEqual(mockMyView)
        })

    })

})
