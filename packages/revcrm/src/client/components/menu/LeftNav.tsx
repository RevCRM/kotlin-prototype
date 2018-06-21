
import * as React from 'react';
import Drawer from '@material-ui/core/Drawer';
import MenuItem from '@material-ui/core/MenuItem';
import { connect } from 'react-redux';
import { IState } from '../../store/index';
import { setLeftNavOpen } from './store/index';
import { Dispatch } from 'redux';
import { viewManager, IMenuItem } from '../../ViewManager';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { RouteComponentProps, withRouter } from 'react-router-dom';

export interface ILeftNavStateProps {
    isOpen: boolean;
}

export interface ILeftNavDispatchProps extends RouteComponentProps<any> {
    onClose: () => void;
}

class LeftNavC extends React.Component<ILeftNavStateProps & ILeftNavDispatchProps> {

    onMenuClick(item: IMenuItem) {
        this.props.history.push(item.url);
        this.props.onClose();
    }

    render() {
        const menuItems = viewManager.getMenus();
        return (
            <Drawer
                open={this.props.isOpen}
                onClose={this.props.onClose}
            >
                <div style={{ background: '#4054B2', height: 65, marginBottom: 10 }}></div>
                {menuItems.map((item) => (
                    <MenuItem key={item.text} onClick={() => this.onMenuClick(item)}>
                        <ListItemIcon>{item.icon}</ListItemIcon>
                        <ListItemText inset primary={item.text} />
                    </MenuItem>
                ))}
            </Drawer>
        );
    }
}

function mapStateToProps(state: IState): ILeftNavStateProps {
    return {
        isOpen: state.menu.leftNavOpen
    };
}

function mapDispatchToProps(dispatch: Dispatch<any>): Partial<ILeftNavDispatchProps> {
    return {
        onClose: () => {
            dispatch(setLeftNavOpen(false));
        }
    };
}

export const LeftNav = withRouter(connect(mapStateToProps, mapDispatchToProps as any)(LeftNavC));
