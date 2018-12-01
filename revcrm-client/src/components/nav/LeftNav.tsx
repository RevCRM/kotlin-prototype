import * as React from 'react';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { Divider, createStyles, withStyles, WithStyles, Icon } from '@material-ui/core';

const styles = createStyles({
    listItemText: {
        padding: 0,
        whiteSpace: 'nowrap'
    }
});

export interface IMenuItem {
    label: string;
    perspective?: string;
    view?: string;
    icon: string;
    subItems?: IMenuSubItem[];
}

export interface IMenuSubItem {
    label: string;
    perspective: string;
    view?: string;
}

// TODO: drive this from metadata
export const MENU: IMenuItem[] = [
    {
        label: 'Dashboard',
        perspective: 'dashboard',
        icon: 'insert_chart'
    },
    {
        label: 'Calendar',
        perspective: 'calendar',
        icon: 'insert_invitation'
    },
    {
        label: 'Companies & Contacts',
        perspective: 'accounts',
        icon: 'supervised_user_circle'
    },
    {
        label: 'Sales & Marketing',
        perspective: 'opportunities',
        icon: 'monetization_on_sharp'
    },
    {
        label: 'Customer Service',
        perspective: 'opportunities',
        icon: 'assignment'
    }
];

export const LeftNav = withStyles(styles)((props: WithStyles<typeof styles>) => (
    <List>
        {MENU.map((menuItem) => (
            <ListItem button>
                <ListItemIcon><Icon>{menuItem.icon}</Icon></ListItemIcon>
                <ListItemText
                    primary={menuItem.label}
                    classes={{ root: props.classes.listItemText }}
                />
            </ListItem>
        ))}
        <Divider />
        <ListItem button>
            <ListItemIcon><Icon>settings</Icon></ListItemIcon>
            <ListItemText
                primary="Settings"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
        <ListItem button>
            <ListItemIcon><Icon>power_settings_new</Icon></ListItemIcon>
            <ListItemText
                primary="Log Out"
                classes={{ root: props.classes.listItemText }}
            />
        </ListItem>
    </List>
));
