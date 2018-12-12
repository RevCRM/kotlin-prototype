
import { IMenuItem } from './components/nav/types';

export interface IPerspective {
    id: string;
    title: string;
    views: {
        [perspectiveViewId: string]: IPerspectiveView;
    };
}

export interface IPerspectiveView {
    title: string;
    viewId: string;
}

export interface IView {
    id: string;
    model: string | null;
    component: React.ComponentType;
}

/**
 * Manages the collection of available perspectives and views
 */
export class UIManager {
    _perspectives: {
        [perspectiveId: string]: IPerspective;
    } = {};
    _views: {
        [viewId: string]: IView;
    } = {};
    _menus: IMenuItem[] = [];

    constructor(
        public homeUrl: string
    ) {}

    registerView(view: IView) {
        if (this._views[view.id]) {
            throw new Error(`View '${view.id}' already registered!`);
        }
        else {
            this._views[view.id] = view;
        }
    }

    registerPerspective(perspective: IPerspective) {
        if (this._perspectives[perspective.id]) {
            throw new Error(`View '${perspective.id}' already registered!`);
        }
        else {
            this._perspectives[perspective.id] = perspective;
        }
    }

    registerMenu(menuItem: IMenuItem) {
        this._menus.push(menuItem);
    }

    getPerspective(perspectiveId: string) {
        const perspective = this._perspectives[perspectiveId];
        if (perspective) {
            return perspective;
        }
        return null;
    }

    getPerspectiveView(perspectiveId: string, perspectiveViewId: string) {
        const perspective = this._perspectives[perspectiveId];
        if (perspective && perspective.views[perspectiveViewId]) {
            return perspective.views[perspectiveViewId];
        }
        return null;
    }

    getView(viewId: string) {
        const view = this._views[viewId];
        if (view) {
            return view;
        }
        return null;
    }

    getMenus() {
        return this._menus;
    }
}

// TODO: Home URL should come from settings
export const UI = new UIManager('/dashboard/my');
