
import { Action, createStore, combineReducers, Store } from 'redux';
import { menuReducer } from '../components/menu/store';
import { IMenuState } from '../components/menu/store/index';

export interface IAction extends Action {
    payload: any;
}

export interface IState {
    form: any;
    menu: IMenuState;
}

export function getStore(): Store<IState> {
    const store = createStore(
        combineReducers({
            menu: menuReducer
        }),
        window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
    );
    return store as any;
}
