import { IAction } from '../../../store/index';

export interface IMenuState {
    leftNavOpen: boolean;
}

export const initialState: IMenuState = {
    leftNavOpen: false
};

export const SET_LEFT_NAV_OPEN = '@menu/SET_LEFT_NAV_OPEN';

export function menuReducer(state: IMenuState = initialState, action: IAction): IMenuState {
    switch (action.type) {
        case SET_LEFT_NAV_OPEN:
            return {
                leftNavOpen: action.payload
            };
        default:
            return state;
    }
}

export function setLeftNavOpen(isOpen: boolean): IAction {
    return {
        type: SET_LEFT_NAV_OPEN,
        payload: isOpen
    };
}
