
import * as React from 'react';
import Drawer from 'material-ui/Drawer';
import { MenuItem } from 'material-ui/Menu';
import { connect } from 'react-redux';
import { IState } from '../../store/index';
import { setLeftNavOpen } from './store/index';
import { Dispatch } from 'redux';

export interface ILeftNavStateProps {
    isOpen: boolean;
}

export interface ILeftNavDispatchProps {
    onClose: (event: any) => void;
}

export class LeftNavC extends React.Component<ILeftNavStateProps & ILeftNavDispatchProps> {
    render() {
        return (
            <Drawer
                open={this.props.isOpen}
                onClose={this.props.onClose}
            >
                <MenuItem>Menu Item</MenuItem>
                <MenuItem>Menu Item 2</MenuItem>
            </Drawer>
        );
    }
}

function mapStateToProps(state: IState): ILeftNavStateProps {
    return {
        isOpen: state.menu.leftNavOpen
    };
}

function mapDispatchToProps(dispatch: Dispatch<any>): ILeftNavDispatchProps {
    return {
        onClose: (event: any) => {
            dispatch(setLeftNavOpen(false));
        }
    };
}

export const LeftNav = connect(mapStateToProps, mapDispatchToProps as any)(LeftNavC) as any;
