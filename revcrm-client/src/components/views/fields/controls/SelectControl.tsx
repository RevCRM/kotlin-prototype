
import * as React from "react"

import Grid from "@material-ui/core/Grid"
import FormControl from "@material-ui/core/FormControl"
import FormHelperText from "@material-ui/core/FormHelperText"
import InputLabel from "@material-ui/core/InputLabel"
import Select from "@material-ui/core/Select"
import MenuItem from "@material-ui/core/MenuItem"
import { IFieldComponentProps } from "./props"
import { getGridWidthProps } from "../../Grid"
import { LoadState } from "../../../utils/types"
import gql from "graphql-tag"
import { withApolloClient, IApolloClientProp } from "../../../../graphql/withApolloClient"
import { ReadOnlyValue } from "./ReadOnlyValue"

const OPTIONS_QUERY = gql`
    query ($code: String!) {
        SelectionList ( where: { code: $code }) {
            results {
                code
                label
                options {
                    code
                    label
                }
            }
        }
    }
`

export interface ISelectionList {
    code: string
    label: string
    options: ISelectionOption[]
}

export interface ISelectionOption {
    code: string
    label: string
}

export interface IOptionsQueryResponse {
    SelectionList: {
        results: ISelectionList[]
    }
}

export interface ISelectControlProps extends
    IFieldComponentProps,
    IApolloClientProp {
}

export interface ISelectControlState {
    loadState: LoadState
    options: ISelectionOption[]
}

export const SelectControl = withApolloClient(
    class extends React.Component<ISelectControlProps, ISelectControlState> {

        constructor(props: any) {
            super(props)
            this.state = {
                loadState: "not_loaded",
                options: []
            }
        }

        async initialise() {
            this.setState({ loadState: "loading" })
            const res = await this.props.client.query<IOptionsQueryResponse>({
                query: OPTIONS_QUERY,
                variables: {
                    code: this.props.field.constraints.SelectionList
                }
            })
            if (res.errors && res.errors.length) {
                this.setState({ loadState: "load_error" })
                console.error("Failed to load options", res.errors)
            }
            else {
                console.log("options loaded", res.data)
                this.setState({
                    loadState: "loaded",
                    options: res.data.SelectionList.results[0].options
                })
            }
        }

        async componentDidMount() {
            this.initialise()
        }

        render() {

            const fieldId = this.props.field.name

            const hasErrors = this.props.errors.length > 0
            let errorText = ""
            this.props.errors.forEach((err) => {
                errorText += err.message + ". "
            })

            const opts = this.state.options

            const style = {
                minHeight: 64,
                ...this.props.style
            }

            const control = (
                <FormControl fullWidth>
                    {!this.props.noLabel &&
                        <InputLabel
                            htmlFor={fieldId}
                            error={hasErrors}
                            shrink={true}
                        >
                            {this.props.label}
                        </InputLabel>
                    }
                    {!this.props.readonly &&
                    <Select
                        value={this.props.value || ""}
                        onChange={(event) => this.props.onChange(event.target.value || null)}
                        inputProps={{
                            id: fieldId
                        }}
                        error={hasErrors}
                        disabled={this.props.disabled}
                    >
                        <MenuItem dense value=""></MenuItem>
                        {opts.map(({ code, label }, index) => (
                            <MenuItem dense key={index} value={code}>{label}</MenuItem>
                        ))}
                    </Select>}
                    {this.props.readonly &&
                    <ReadOnlyValue>{this.props.value || ""}</ReadOnlyValue>}
                    {errorText &&
                    <FormHelperText error>
                        {errorText}
                    </FormHelperText>}
                </FormControl>
            )

            if (this.props.grid) {
                const gridWidthProps = getGridWidthProps(this.props.grid)
                return (
                    <Grid item {...gridWidthProps} style={style}>
                        {control}
                    </Grid>
                )
            }
            else {
                return control
            }
        }
    })
