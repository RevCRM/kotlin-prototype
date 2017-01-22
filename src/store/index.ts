
import { Action, createStore, combineReducers } from 'redux';
import menuReducer from '../components/menu/store';
import { IMenuState } from '../components/menu/store/index';

export interface IAction extends Action {
    payload: any;
}

export interface IState {
    menu: IMenuState;
}

export function getStore() {
    const store = createStore(
        combineReducers({
            menu: menuReducer
        }),
        window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
    );
    return store;
}
