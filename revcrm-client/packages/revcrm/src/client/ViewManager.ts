
export interface IView {
    name: string;
    model: string;
    related?: string[];
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

export interface IMenuSubItem {
    text: string;
    url: string;
}

export interface IMenuItem {
    text: string;
    url: string;
    icon: React.ReactElement<any>;
    subItems?: IMenuSubItem[];
}

export class ViewManager {
    _views: IViewsDefinition = {
        perspectives: {},
        views: {}
    };
    _menus: IMenuItem[] = [];

    registerView(view: IView) {
        if (this._views.views[view.name]) {
            throw new Error(`View '${view.name}' already registered!`);
        }
        else {
            this._views.views[view.name] = view;
        }
    }

    registerPerspective(perspective: IPerspective) {
        if (this._views.perspectives[perspective.name]) {
            throw new Error(`View '${perspective.name}' already registered!`);
        }
        else {
            this._views.perspectives[perspective.name] = perspective;
        }
    }

    registerMenu(menuItem: IMenuItem) {
        this._menus.push(menuItem);
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

    getMenus() {
        return this._menus;
    }
}

// TODO: This shouldn't be static
export const viewManager = new ViewManager();
