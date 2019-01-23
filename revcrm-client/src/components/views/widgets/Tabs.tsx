
import * as React from "react"
import MUITabs from "@material-ui/core/Tabs"
import Tab from "@material-ui/core/Tab"
import Card from "@material-ui/core/Card"
import { IGridProps, GridItem, Grid } from "../Grid"
import { AppBar, CardContent } from "@material-ui/core"

export interface ITabDefinition {
    label: string
    disableGrid?: boolean
    component: React.ComponentType
}

export interface ITabsProps extends IGridProps {
    tabs: ITabDefinition[]
}

export interface ITabsState {
    selectedTab: number
}

export class Tabs extends React.Component<ITabsProps, ITabsState> {

    constructor(props: any) {
        super(props)
        this.state = {
            selectedTab: 0
        }
        this.handleChange = this.handleChange.bind(this)
    }

    handleChange(event: any, value: any) {
        this.setState({ selectedTab: value })
    }

    render() {
        const selectedTab = this.props.tabs[this.state.selectedTab]
        const Component = selectedTab.component
        return (
            <GridItem {...this.props}>
                <Card>
                    <AppBar position="static" color="default">
                        <MUITabs
                            value={this.state.selectedTab}
                            indicatorColor="primary"
                            onChange={this.handleChange}
                        >
                        {this.props.tabs.map((tab) => (
                            <Tab key={tab.label} label={tab.label} />
                        ))}
                        </MUITabs>
                    </AppBar>
                    {selectedTab.disableGrid
                        ? <Component />
                        : <CardContent>
                            <Grid>
                                <Component />
                            </Grid>
                        </CardContent>
                    }
                </Card>
            </GridItem>
        )
    }
}
