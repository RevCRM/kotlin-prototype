
import { IMenuItem } from './components/nav/types';

export interface IPerspective {
    id: string;
    title: string;
    views: {
        [viewId: string]: string;
    };
}

export interface IView {
    id: string;
    model: string | null;
    component: React.ReactNode;
}

export class UIManager {
    _perspectives: {
        [perspectiveId: string]: IPerspective;
    } = {};
    _views: {
        [viewId: string]: IView;
    } = {};
    _menus: IMenuItem[] = [];

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

    getPerspectiveViewName(perspectiveId: string, viewId: string) {
        const perspective = this._perspectives[perspectiveId];
        if (perspective && perspective.views[viewId]) {
            return perspective.views[viewId];
        }
        throw new Error(`Perspective view '${perspectiveId}/${viewId}' is not defined`);
    }

    getPerspective(perspectiveId: string) {
        const perspective = this._perspectives[perspectiveId];
        if (perspective) {
            return perspective;
        }
        throw new Error(`Perspective '${perspectiveId}' is not defined`);
    }

    getView(viewId: string) {
        const view = this._views[viewId];
        if (view) {
            return view;
        }
        throw new Error(`View '${viewId}' is not defined`);
    }

    getMenus() {
        return this._menus;
    }
}

export const UI = new UIManager();
