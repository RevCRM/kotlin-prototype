import * as React from "react"
import { Theme, createStyles, withStyles, WithStyles } from "@material-ui/core"
import { FilterBar } from "./FilterBar"
import { ListView } from "./ListView"
import { IMetadataContextProp, withMetadataContext } from "../data/Metadata"
import { IViewManagerContextProp, withViewManagerContext } from "./ViewManager"
import {Field} from "./fields/Field"

export const styles = (theme: Theme) => createStyles({
    root: {
    },
    resultsBox: {
        backgroundColor: "#fff"
    },
})

export interface ISearchViewProps extends
    IMetadataContextProp,
    IViewManagerContextProp,
    WithStyles<typeof styles> {
    title: string
    entity: string
    showFields: string[]
    detailView?: string
}

export interface ISearchViewState {
    where: object
}

export const SearchView = withStyles(styles)(withMetadataContext(withViewManagerContext(
    class extends React.Component<ISearchViewProps, ISearchViewState> {
        perspectiveWhere: object

        constructor(props: any) {
            super(props)
            const { perspective } = this.props.view
            this.perspectiveWhere = perspective.where ? perspective.where : {}
            this.state = {
                where: {...this.perspectiveWhere}
            }
        }

        onFilter = (filterWhere: object) => {
            const where = {
                ...filterWhere,
                ...this.perspectiveWhere
            }
            this.setState({ where })
        }

        render() {
            return (
                <div className={this.props.classes.root}>
                    <FilterBar
                        entity={this.props.entity}
                        searchPlaceholderText={"Search " + this.props.title}
                        onFilter={this.onFilter}
                        newRecordView={this.props.detailView}
                    />
                    <div className={this.props.classes.resultsBox}>
                        <ListView
                            entity={this.props.entity}
                            title={this.props.title}
                            where={this.state.where}
                            detailView={this.props.detailView}
                        >
                            {this.props.showFields.map((field, idx) => (
                                <Field name={field} key={idx} />
                            ))}
                        </ListView>
                    </div>
                </div>
            )
        }
    })))
