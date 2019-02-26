
import * as React from "react"
import { IViewContext } from "./types"
import { IAction, IPerspective, IView, UI } from "../../UIManager"
import { History, Location, Action } from "history"
import { Omit } from "../../types"
import { buildQueryString, queryStringToObject } from "../../utils/urls"

export interface IViewManagerLocation {
    pathname: string
    search: string
}

export interface IViewManagerContext {
    history: History<any>
    perspective: IPerspective
    viewName: string
    view: IView
    context: IViewContext

    changePerspective(perspectiveId: string, viewId?: string, context?: IViewContext): void
    runAction(action: IAction): void
}

export interface IViewManagerProps {
    history: History<any>
}

export interface IViewManagerState {
    perspective: IPerspective
    viewName: string
    view: IView
    context: IViewContext
}

// TODO: Move this somewhere better
export function getViewStateFromUrl(pathname: string, search: string) {
    const [urlPerspectiveId, urlViewName] = pathname.split("/").slice(1)
    let perspective: IPerspective = null as any
    let viewName: string = null as any
    let view: IView = null as any
    perspective = UI.getPerspective(urlPerspectiveId)!
    if (perspective && urlViewName) {
        const perspectiveView = perspective.views[urlViewName]
        if (perspectiveView) {
            view = UI.getView(perspectiveView.viewId)!
            viewName = view ? urlViewName : null as any
        }
    }
    else if (perspective) {
        const perspectiveView = perspective.views[perspective.defaultView]
        if (perspectiveView) {
            view = UI.getView(perspectiveView.viewId)!
            viewName = view ? perspective.defaultView : null as any
        }
    }
    const context = queryStringToObject(search)
    return {
        perspective,
        viewName,
        view,
        context
    }
}

export const ViewManagerContext = React.createContext<IViewManagerContext>(null as any)

export class ViewManager extends React.Component<IViewManagerProps, IViewManagerState> {
    _locationUnlisten: any

    constructor(props: any) {
        super(props)
        const initialState = getViewStateFromUrl(
            this.props.history.location.pathname,
            this.props.history.location.search
        )
        this.state = initialState
    }

    onLocationChanged = (location: Location, action: Action) => {
        const { pathname, search } = location
        this.setState({
            ...getViewStateFromUrl(pathname, search)
        })
    }

    componentWillMount() {
        this._locationUnlisten = this.props.history.listen(this.onLocationChanged)
    }

    componentWillUnmount() {
        this._locationUnlisten()
    }

    changePerspective = (perspectiveId: string, perspectiveViewId?: string, context?: IViewContext) => {
        const query = context ? "?" + buildQueryString(context) : ""
        let url = "/" + perspectiveId
        if (perspectiveViewId) {
            url += "/" + perspectiveViewId
        }
        url += query
        this.props.history.push(url)
    }

    // TODO: Move this to an ActionManager, or something :)
    runAction = (action: IAction) => {
        if (action.type == "open_view") {
            this.changePerspective(action.perspective, action.viewName, this.state.context)
        }
    }

    render() {
        const ctx: IViewManagerContext = {
            history: this.props.history,
            ...this.state,
            changePerspective: this.changePerspective,
            runAction: this.runAction
        }
        return (
            <ViewManagerContext.Provider value={ctx}>
                {this.props.children}
            </ViewManagerContext.Provider>
        )
    }

}

export interface IViewManagerContextProp {
    view: IViewManagerContext
}

export function withViewManagerContext<
    TComponentProps extends IViewManagerContextProp,
    TWrapperProps = Omit<TComponentProps, keyof IViewManagerContextProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return (props: any): React.ReactElement<TComponentProps> => (
        <ViewManagerContext.Consumer>{(ctx) => (
            <Component view={ctx} {...props} />
        )}</ViewManagerContext.Consumer>
    )
}
