
import * as React from "react"
import { IViewContext } from "./types"
import { IPerspective, IView, UI, IPerspectiveView } from "../../UIManager"
import { History, Location, Action } from "history"
import { Omit } from "../../types"
import { buildQueryString, queryStringToObject } from "../../utils/urls"

export interface IViewManagerLocation {
    pathname: string
    search: string
}

export interface IViewManagerContext {
    history: History<any>
    perspective: IPerspective | null
    perspectiveView: IPerspectiveView | null
    view: IView | null
    context: IViewContext

    changePerspective(perspectiveId: string, viewId?: string, context?: IViewContext): void
}

export interface IViewManagerProps {
    history: History<any>
}

export interface IViewManagerState {
    perspective: IPerspective | null
    perspectiveView: IPerspectiveView | null
    view: IView | null
    context: IViewContext
}

// TODO: Move this somewhere better
export function getViewStateFromUrl(pathname: string, search: string) {
    const [ perspectiveId, perspectiveViewId ] = pathname.split("/").slice(1)
    const perspective = UI.getPerspective(perspectiveId)
    const perspectiveView = UI.getPerspectiveView(perspectiveId, perspectiveViewId)
    const view = perspectiveView ? UI.getView(perspectiveView.viewId) : null
    const context = queryStringToObject(search)
    return {
        perspective,
        perspectiveView,
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

    changePerspective = (perspectiveId: string, perspectiveViewId: string, context?: IViewContext) => {
        const query = context ? "?" + buildQueryString(context) : ""
        this.props.history.push(`/${perspectiveId}/${perspectiveViewId}${query}`)
    }

    render() {
        const ctx: IViewManagerContext = {
            history: this.props.history,
            ...this.state,
            changePerspective: this.changePerspective
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
