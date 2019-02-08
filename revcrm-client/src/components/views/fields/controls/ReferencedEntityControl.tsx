
import * as React from "react"

import Grid from "@material-ui/core/Grid"
import FormControl from "@material-ui/core/FormControl"
import FormHelperText from "@material-ui/core/FormHelperText"
import InputLabel from "@material-ui/core/InputLabel"
import Select from "@material-ui/core/Select"
import MenuItem from "@material-ui/core/MenuItem"
import { IFieldComponentProps } from "./props"
import { getGridWidthProps, IMUIGridProps } from "../../Grid"
import { LoadState } from "../../../utils/types"
import { withApolloClient, IApolloClientProp } from "../../../../graphql/withApolloClient"
import { ReadOnlyValue } from "./ReadOnlyValue"
import {
    getReferencedEntityQuery,
    IEntityQueryResults,
    IReferencedEntityResult
} from "../../../../graphql/queries"
import {DocumentNode} from "graphql"
import {IEntityMetadata} from "../../../data/Metadata"

export interface IReferencedEntityControlProps extends
    IFieldComponentProps,
    IApolloClientProp {
}

export interface IReferencedEntityOptions {
    id: string
    record_name: string
}

export interface IReferencedEntityControlState {
    loadState: LoadState
    options: IReferencedEntityOptions[]
}

// TODO: Tests

export const ReferencedEntityControl = withApolloClient(
    class extends React.Component<IReferencedEntityControlProps, IReferencedEntityControlState> {
        gridWidthProps: IMUIGridProps
        entity: string
        entityMeta: IEntityMetadata
        query: DocumentNode

        constructor(props: any) {
            super(props)
            this.gridWidthProps = getGridWidthProps(props)

            this.entity = this.props.field.constraints["Entity"]
            // TODO: This should not assume getEntity() returns an entity
            this.entityMeta = this.props.meta.getEntity(this.entity)!

            this.query = getReferencedEntityQuery({
                meta: this.props.meta,
                entity: this.entityMeta
            })

            this.state = {
                loadState: "not_loaded",
                options: []
            }
        }

        async initialise() {
            this.setState({ loadState: "loading" })
            const res = await this.props.client.query<IEntityQueryResults<IReferencedEntityResult>>({
                query: this.query,
                variables: {
                    where: {},  // TODO: Filter list based on search text
                    limit: 100
                }
            })
            if (res.errors && res.errors.length) {
                this.setState({ loadState: "load_error" })
                console.error("Failed to load referenced entity list", res.errors)
            }
            else {
                console.log("referenced entity list loaded", res.data)

                const options: IReferencedEntityOptions[] = res.data[this.entityMeta.name].results.map(r => (
                    { id: r.id, record_name: this.getReferencedEntityName(r) }
                ))

                this.setState({
                    loadState: "loaded",
                    options
                })
            }
        }

        async componentDidMount() {
            this.initialise()
        }

        getReferencedEntityName(r: IReferencedEntityResult): string {
            return r.record_name
                ? r.record_name
                : `${this.entityMeta.name} ${r.id}`
        }

        getSelectValue = (): string => {
            if (this.props.value) {
                return this.props.value[this.entityMeta.idField] || ""
            }
            return ""
        }

        getReadonlyValue = (): string => {
            if (this.props.value) {
                return this.getReferencedEntityName(this.props.value)
            }
            return ""
        }

        onSelectChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
            const idValue = e.target.value
            const selectedOption = idValue
                ? this.state.options.find(o => o.id == idValue)
                : null
            this.props.onChange(selectedOption)
        }

        render() {

            const fieldId = this.props.field.name

            const hasErrors = this.props.errors.length > 0
            let errorText = ""
            this.props.errors.forEach((err) => {
                errorText += err.message + ". "
            })

            const opts = this.state.options
            const value = this.getSelectValue()
            const readOnlyValue = this.getReadonlyValue()

            const style = {
                minHeight: 64,
                ...this.props.style
            }

            return (
                <Grid item {...this.gridWidthProps} style={style}>

                    <FormControl fullWidth>
                        <InputLabel
                            htmlFor={fieldId}
                            error={hasErrors}
                            shrink={true}
                        >
                            {this.props.label}
                        </InputLabel>
                        {!this.props.readonly &&
                            <Select
                                value={value}
                                onChange={this.onSelectChange}
                                inputProps={{
                                    id: fieldId
                                }}
                                error={hasErrors}
                                disabled={this.props.disabled}
                            >
                                <MenuItem dense value=""></MenuItem>
                                {opts.map(({ id, record_name }, index) => (
                                    <MenuItem dense key={index} value={id}>{record_name}</MenuItem>
                                ))}
                            </Select>}
                        {this.props.readonly &&
                            <ReadOnlyValue>{readOnlyValue}</ReadOnlyValue>}
                        {errorText &&
                        <FormHelperText error>
                            {errorText}
                        </FormHelperText>}
                    </FormControl>

                </Grid>
            )
        }
    })
