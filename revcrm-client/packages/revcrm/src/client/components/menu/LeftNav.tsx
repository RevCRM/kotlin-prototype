
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
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';
import { RouteComponentProps, withRouter } from 'react-router-dom';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import Collapse from '@material-ui/core/Collapse';

export interface ILeftNavStateProps {
    isOpen: boolean;
}

export interface ILeftNavDispatchProps extends RouteComponentProps<any> {
    onClose: () => void;
}

interface ILeftNavItemProps {
    item: IMenuItem;
    navigate: (url: string) => void;
}
interface ILeftNavItemState {
    collapsed: boolean;
}

class LeftNavItem extends React.Component<ILeftNavItemProps, ILeftNavItemState> {

    constructor(props: any) {
        super(props);
        this.state = {
            collapsed: true
        };
    }

    handleMainButtonClick(item: IMenuItem) {
        if (!item.subItems) {
            this.props.navigate(item.url);
        }
        else {
            this.setState((state) => ({
                collapsed: !state.collapsed
            }));
        }
    }

    render() {
        const item = this.props.item;
        return (
            <div>
                <MenuItem onClick={() => this.handleMainButtonClick(item)}>
                    <ListItemIcon>{item.icon}</ListItemIcon>
                    <ListItemText inset primary={item.text} />
                    {item.subItems && this.state.collapsed ? <ExpandMore /> : <ExpandLess />}
                </MenuItem>
                {item.subItems &&
                    <Collapse in={!this.state.collapsed} timeout="auto" unmountOnExit>
                        <List component="div" disablePadding style={{padding: '10px 0', background: '#EEE'}}>
                            {item.subItems.map((subItem) => (
                                <ListItem button style={{ padding: '4px 20px 4px 72px' }}
                                    onClick={() => this.props.navigate(subItem.url)}>
                                    <ListItemText primary={subItem.text} />
                                </ListItem>
                            ))}
                        </List>
                    </Collapse>
                }
            </div>
        );
    }

}

class LeftNavC extends React.Component<ILeftNavStateProps & ILeftNavDispatchProps> {

    navigate(url: string) {
        // FIXME this.props.history.push(item.url);
        window.location.href = url;
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
                    <LeftNavItem key={item.text} item={item} navigate={(url) => this.navigate(url)} />
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

export const LeftNav = withRouter(connect(mapStateToProps, mapDispatchToProps as any)(LeftNavC)) as any;
