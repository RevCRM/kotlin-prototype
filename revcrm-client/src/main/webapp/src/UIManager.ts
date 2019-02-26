
export interface IPerspective {
    id: string
    title: string
    entity: string
    where?: object
    views: {
        [viewName: string]: {
            title: string
            viewId: string
            actions?: IAction[]
        };
    }
    defaultView: string
}

export interface IView {
    id: string
    entity: string | null
    component: React.ComponentType
}

export interface IMenuItem {
    id: string
    label: string
    icon: string
    subItems?: IMenuSubItem[]
}

export interface IMenuSubItem {
    label: string
    perspective: string
    viewName?: string
}

export interface IAction {
    label: string
    icon: string
    type: "open_view"  // there will most likely be more types in future
    perspective: string
    viewName: string
}

/**
 * Manages the collection of available perspectives and views
 */
export class UIManager {
    _perspectives: {
        [perspectiveId: string]: IPerspective;
    } = {}
    _views: {
        [viewId: string]: IView;
    } = {}
    _menus: IMenuItem[] = []

    registerView(view: IView) {
        if (this._views[view.id]) {
            throw new Error(`View '${view.id}' already registered!`)
        }
        else {
            this._views[view.id] = view
        }
    }

    registerPerspective(perspective: IPerspective) {
        if (this._perspectives[perspective.id]) {
            throw new Error(`View '${perspective.id}' already registered!`)
        }
        else {
            this._perspectives[perspective.id] = perspective
        }
    }

    registerMenu(menuItem: IMenuItem) {
        this._menus.push(menuItem)
    }

    getPerspective(perspectiveId: string) {
        const perspective = this._perspectives[perspectiveId]
        if (perspective) {
            return perspective
        }
        return null
    }

    getView(viewId: string) {
        const view = this._views[viewId]
        if (view) {
            return view
        }
        return null
    }

    getMenus() {
        return this._menus
    }
}

// TODO: Home URL should come from settings
export const UI = new UIManager()
