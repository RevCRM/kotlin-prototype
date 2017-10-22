
import * as React from 'react';
import Drawer from 'material-ui/Drawer';
import MenuItem from 'material-ui/MenuItem';
import { connect } from 'react-redux';
import { IState } from '../../store/index';
import { setLeftNavOpen } from './store/index';
import { Dispatch } from 'redux';

export interface ILeftNavStateProps {
    isOpen: boolean;
}

export interface ILeftNavDispatchProps {
    onRequestChange: (open: boolean, reason: string) => void;
}

export class LeftNavC extends React.Component<ILeftNavStateProps & ILeftNavDispatchProps> {
    render() {
        return (
            <Drawer
                docked={false}
                width={200}
                open={this.props.isOpen}
                onRequestChange={this.props.onRequestChange}
            >
                <MenuItem onTouchTap={null}>Menu Item</MenuItem>
                <MenuItem onTouchTap={null}>Menu Item 2</MenuItem>
            </Drawer>
        );
    }
}

function mapStateToProps(state: IState): ILeftNavStateProps {
    return {
        isOpen: state.menu.leftNavOpen
    };
}

function mapDispatchToProps(dispatch: Dispatch<IState>): ILeftNavDispatchProps {
    return {
        onRequestChange: (open: boolean, reason: string) => {
            dispatch(setLeftNavOpen(open));
        }
    };
}

export const LeftNav = connect(mapStateToProps, mapDispatchToProps)(LeftNavC) as any;
