import * as React from "react"
import List from "@material-ui/core/List"
import ListItem from "@material-ui/core/ListItem"
import ListItemIcon from "@material-ui/core/ListItemIcon"
import ListItemText from "@material-ui/core/ListItemText"
import { Divider, createStyles, withStyles, WithStyles, Icon, Collapse, Typography } from "@material-ui/core"
import { IMenuItem, IMenuSubItem } from "./types"
import { UI } from "../../UIManager"
import { withViewManagerContext, IViewManagerContextProp } from "../views/ViewManager"

const styles = createStyles({
    listItemText: {
        padding: 0,
        whiteSpace: "nowrap"
    },
    subItemList: {
        background: "#F2F2F2"
    },
    selectedSubItem: {
        background: "#BBB"
    }
})

export interface ILeftNavProps extends
    WithStyles<typeof styles>,
    IViewManagerContextProp {

}

export interface ILeftNavState {
    expandedMenus: {
        [id: string]: boolean
    }
}

export const LeftNav = withStyles(styles)(withViewManagerContext(
    class extends React.Component<ILeftNavProps, ILeftNavState> {

        constructor(props: any) {
            super(props)
            this.state = {
                expandedMenus: {}
            }
        }

        onMainButtonClick(item: IMenuItem) {
            this.setState((state) => ({
                expandedMenus: {
                    ...state.expandedMenus,
                    [item.id]: !state.expandedMenus[item.id]
                }
            }))
        }

        onSubItemClick(item: IMenuSubItem) {
            this.props.view.changePerspective(
                item.perspective, item.view
            )
        }

        render() {
            const MENU = UI.getMenus()

            const perspectiveId = this.props.view.perspective.id
            const viewName = this.props.view.viewName

            return (
                <List>
                    {MENU.map((item) => {
                        const expanded = Boolean(this.state.expandedMenus[item.id])
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
                                    <List component="div" disablePadding className={this.props.classes.subItemList}>
                                        {item.subItems.map((subItem, subItemIdx) => {
                                            const isSelected = item.perspective == perspectiveId && subItem.view == viewName
                                            return (
                                                <ListItem button key={subItemIdx}
                                                    style={{ padding: 8, paddingLeft: 64 }}
                                                    className={isSelected ? this.props.classes.selectedSubItem : undefined}
                                                    onClick={() => this.onSubItemClick(subItem)}>
                                                    <Typography variant="body2">{subItem.label}</Typography>
                                                </ListItem>
                                            )
                                        })}
                                    </List>
                                </Collapse>
                            }
                        </div>)
                    })}
                    <Divider />
                    <ListItem button>
                        <ListItemIcon><Icon>settings</Icon></ListItemIcon>
                        <ListItemText
                            primary="System Administration"
                            classes={{ root: this.props.classes.listItemText }}
                        />
                    </ListItem>
                </List>
            )

        }
    }))
