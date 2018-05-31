
export interface IView {
    name: string;
    model: string;
    component: React.ReactNode;
}

export interface IPerspective {
    name: string;
    title: string;
    views: {
        [viewName: string]: string;
    };
}

export interface IViewsDefinition {
    perspectives: {
        [perspectiveName: string]: IPerspective;
    };
    views: {
        [viewName: string]: IView;
    };
}

export interface IViewMeta {
    perspective: IPerspective;
    view: {
        [viewName: string]: IView;
    };
}

export class ViewManager {
    _views: IViewsDefinition = {
        perspectives: {},
        views: {}
    };

    registerView(name: string, view: IView) {
        if (this._views.views[name]) {
            throw new Error(`View '${name}' already registered!`);
        }
        else {
            this._views.views[name] = view;
        }
    }

    registerPerspective(name: string, perspective: IPerspective) {
        if (this._views.perspectives[name]) {
            throw new Error(`View '${name}' already registered!`);
        }
        else {
            this._views.perspectives[name] = perspective;
        }
    }

    getPerspectiveViewName(perspectiveName: string, view: string) {
        const perspective = this._views.perspectives[perspectiveName];
        if (perspective && perspective.views[view]) {
            return perspective.views[view];
        }
        throw new Error(`Perspective view '${perspectiveName}/${view}' is not defined`);
    }

    getPerspective(perspectiveName: string) {
        const perspective = this._views.perspectives[perspectiveName];
        if (perspective) {
            return perspective;
        }
        throw new Error(`Perspective '${perspectiveName}' is not defined`);
    }

    getView(viewName: string) {
        const view = this._views.views[viewName];
        if (view) {
            return view;
        }
        throw new Error(`View '${view}' is not defined`);
    }
}

// TODO: This shouldn't be static
export const viewManager = new ViewManager();
