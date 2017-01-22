
import * as React from 'react';
import Drawer from 'material-ui/Drawer';
import MenuItem from 'material-ui/MenuItem';
import { connect } from 'react-redux';
import { IState } from '../../store/index';
import { setLeftNavOpen } from './store/index';

export interface ILeftNavProps {
    isOpen: boolean;
    onRequestChange: (open: boolean, reason: string) => void;
}

class LeftNav extends React.Component<ILeftNavProps, void> {
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

function mapStateToProps(state: IState) {
    return {
        isOpen: state.menu.leftNavOpen
    };
}

function mapDispatchToProps(dispatch: any) {
    return {
        onRequestChange: (open: boolean, reason: string) => {
            dispatch(setLeftNavOpen(open));
        }
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(LeftNav);
