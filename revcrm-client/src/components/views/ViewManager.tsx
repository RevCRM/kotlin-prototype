
import * as React from 'react';
import { IViewContext } from './types';
import { IPerspective, IView, UI, IPerspectiveView } from '../../UIManager';
import { History, Location, Action, UnregisterCallback } from 'history';
import { Omit } from '../../types';

export interface IViewManagerLocation {
    pathname: string;
    search: string;
}

export interface IViewManagerContext {
    history: History<any>;
    perspective: IPerspective;
    perspectiveView: IPerspectiveView;
    view: IView;
    viewContext: IViewContext;

    changePerspective(perspectiveId: string, viewId: string, context?: IViewContext): void;
}

export interface IViewManagerProps {
    history: History<any>;
}

export interface IViewManagerState {
    perspective: IPerspective;
    perspectiveView: IPerspectiveView;
    view: IView;
    viewContext: IViewContext;
}

export const ViewManagerContext = React.createContext<IViewManagerContext>(null as any);

export class ViewManager extends React.Component<IViewManagerProps, IViewManagerState> {
    _locationUnlisten: any;

    constructor(props: any) {
        super(props);

        if (this.props.history.location.pathname == '/') {
            this.props.history.replace(UI.homeUrl);
        }

        this.state = {
            ...this.getViewManagerStateFromLocation(this.props.history.location)
        };

    }

    getViewManagerStateFromLocation(location: IViewManagerLocation): IViewManagerState {
        const { pathname } = location;
        const [ perspectiveId, perspectiveViewId ] = pathname.split('/').slice(1);
        return this.getViewManagerState(perspectiveId, perspectiveViewId);
    }

    getViewManagerState(perspectiveId: string, perspectiveViewId: string): IViewManagerState {
        const perspective = UI.getPerspective(perspectiveId);
        const perspectiveView = UI.getPerspectiveView(perspectiveId, perspectiveViewId);
        const view = UI.getView(perspectiveView.viewId);

        return {
            perspective,
            perspectiveView,
            view,
            viewContext: {
                model: '',
                modelId: null
            }
        };
    }

    onLocationChanged = (location: Location, action: Action) => {
        this.setState({
            ...this.getViewManagerStateFromLocation(location)
        });
    }

    componentWillMount() {
        this._locationUnlisten = this.props.history.listen(this.onLocationChanged);
    }

    componentWillUnmount() {
        this._locationUnlisten();
    }

    changePerspective = (perspectiveId: string, perspectiveViewId: string, context?: IViewContext) => {
        this.props.history.push(`/${perspectiveId}/${perspectiveViewId}`);
    }

    render() {
        const ctx: IViewManagerContext = {
            history: this.props.history,
            ...this.state,
            changePerspective: this.changePerspective
        };
        return (
            <ViewManagerContext.Provider value={ctx}>
                {this.props.children}
            </ViewManagerContext.Provider>
        );
    }

}

export interface IViewManagerContextProp {
    viewManagerCtx: IViewManagerContext;
}

export function withViewManagerContext<
    TComponentProps extends IViewManagerContextProp,
    TWrapperProps = Omit<TComponentProps, keyof IViewManagerContextProp>
>(
    Component: React.ComponentType<TComponentProps>
): React.ComponentType<TWrapperProps> {
    return (props: TWrapperProps): React.ReactElement<TComponentProps> => (
        <ViewManagerContext.Consumer>{(ctx) => (
            <Component viewManagerCtx={ctx} {...props} />
        )}</ViewManagerContext.Consumer>
    );
}
