import * as React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { Divider, createStyles, withStyles, WithStyles, Icon, Collapse, Typography } from '@material-ui/core';
import { IMenuItem } from './types';

const styles = createStyles({
    listItemText: {
        padding: 0,
        whiteSpace: 'nowrap'
    }
});

// TODO: drive this from metadata
export const MENU: IMenuItem[] = [
    {
        id: 'menu_dashboard',
        label: 'Dashboard',
        perspective: 'dashboard',
        icon: 'insert_chart',
        subItems: [
            { label: 'My Dashboard', perspective: 'dashboard', view: 'my' },
            { label: 'Team Dashboard', perspective: 'dashboard', view: 'team' },
        ]
    },
    {
        id: 'menu_calendar',
        label: 'Calendar',
        perspective: 'calendar',
        icon: 'insert_invitation',
        subItems: [
            { label: 'My Calendar', perspective: 'calendar', view: 'my' },
            { label: 'Team Calendar', perspective: 'calendar', view: 'team' },
        ]
    },
    {
        id: 'menu_accounts',
        label: 'Companies & Contacts',
        perspective: 'accounts',
        icon: 'supervised_user_circle',
        subItems: [
            { label: 'Companies', perspective: 'accounts', view: 'companies' },
            { label: 'Contacts', perspective: 'accounts', view: 'contacts' },
            { label: 'Leads', perspective: 'accounts', view: 'leads' },
            { label: 'Data Import', perspective: 'accounts', view: 'import' },
        ]
    },
    {
        id: 'menu_opportunities',
        label: 'Sales & Marketing',
        perspective: 'opportunities',
        icon: 'monetization_on_sharp',
        subItems: [
            { label: 'Sales Opportunities', perspective: 'opportunities', view: 'list' },
        ]
    },
    {
        id: 'menu_cases',
        label: 'Customer Service',
        perspective: 'cases',
        icon: 'assignment',
        subItems: [
            { label: 'My Cases', perspective: 'cases', view: 'my' },
            { label: 'Team Cases', perspective: 'cases', view: 'team' },
        ]
    }
];

export interface ILeftNavState {
    expandedMenus: {
        [id: string]: boolean
    };
}

export const LeftNav = withStyles(styles)(
    class extends React.Component<WithStyles<typeof styles>, ILeftNavState> {

    constructor(props: any) {
        super(props);
        this.state = {
            expandedMenus: {}
        };
    }

    onMainButtonClick(item: IMenuItem) {
        this.setState((state) => ({
            expandedMenus: {
                ...state.expandedMenus,
                [item.id]: !state.expandedMenus[item.id]
            }
        }));
    }

    render() {
        return (
            <List>
                {MENU.map((item) => {
                    const expanded = Boolean(this.state.expandedMenus[item.id]);
                    return (<div key={item.id}>
                        <ListItem button onClick={() => this.onMainButtonClick(item)}>
                            <ListItemIcon><Icon>{item.icon}</Icon></ListItemIcon>
                            <ListItemText
                                primary={item.label}
                                classes={{ root: this.props.classes.listItemText }}
                            />
                        </ListItem>
                        {item.subItems &&
                            <Collapse in={expanded} timeout="auto" unmountOnExit>
                                <List component="div" disablePadding style={{ background: '#F2F2F2' }}>
                                    {item.subItems.map((subItem) => (
                                        <ListItem button style={{ padding: 8, paddingLeft: 64 }}
                                            onClick={() => alert(subItem.label)}>
                                            <Typography variant="body2">{subItem.label}</Typography>
                                        </ListItem>
                                    ))}
                                </List>
                            </Collapse>
                        }
                    </div>);
                })}
                <Divider />
                <ListItem button>
                    <ListItemIcon><Icon>settings</Icon></ListItemIcon>
                    <ListItemText
                        primary="Settings"
                        classes={{ root: this.props.classes.listItemText }}
                    />
                </ListItem>
                <ListItem button>
                    <ListItemIcon><Icon>power_settings_new</Icon></ListItemIcon>
                    <ListItemText
                        primary="Log Out"
                        classes={{ root: this.props.classes.listItemText }}
                    />
                </ListItem>
            </List>
        );

    }
});
