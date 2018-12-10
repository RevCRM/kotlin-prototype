
import * as React from 'react';
import { IViewContext } from './types';
import { IPerspective, IView } from '../../UIManager';
import { History } from 'history';

export interface IViewManagerContext {
    location: {
        pathname: string;
        search: string;
    };
    perspective: IPerspective;
    view: IView;
    viewContext: IViewContext;

    changePerspective(perspectiveId: string, viewId: string, context?: IViewContext): void;
}

export interface IViewManagerProps {
    history: History<any>;
}

export interface IViewManagerState {
    location: {
        pathname: string;
        search: string;
    };
    perspective: IPerspective;
    view: IView;
    viewContext: IViewContext;
}

export const ViewManagerContext = React.createContext<IViewManagerContext>(null as any);

export class ViewManager extends React.Component<IViewManagerProps, IViewManagerState> {

    constructor(props: any) {
        super(props);
        const { pathname, search } = this.props.history.location;
        this.state = {
            location: {
                pathname,
                search
            },
            perspective: null as any,
            view: null as any,
            viewContext: {
                model: '',
                modelId: null
            }
        };
    }

    changePerspective = (perspectiveId: string, viewId: string, context?: IViewContext) => {
        console.log('changing perspective...');
    }

    render() {
        const ctx: IViewManagerContext = {
            ...this.state,
            changePerspective: this.changePerspective
        };
        return this.props.children;
    }

}
