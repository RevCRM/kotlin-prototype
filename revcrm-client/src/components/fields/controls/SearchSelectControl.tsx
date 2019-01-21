import React from "react"
import Autosuggest, { SuggestionSelectedEventData, BlurEvent } from "react-autosuggest"
import Paper from "@material-ui/core/Paper"
import MenuItem from "@material-ui/core/MenuItem"
import { withStyles, Theme, createStyles, WithStyles } from "@material-ui/core/styles"
import { IFieldComponentProps } from "./props"
import { Input, Grid, FormControl, InputLabel, InputAdornment, Icon } from "@material-ui/core"
import { getGridWidthProps, IMUIGridProps } from "../../views/Grid"
import { ISelectionOption, IOptionsQueryResponse } from "./SelectControl"
import gql from "graphql-tag"
import { IApolloClientProp, withApolloClient } from "../../../graphql/withApolloClient"
import { LoadState } from "../../utils/types"
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

export const styles = (theme: Theme) => createStyles({
    container: {
        position: "relative",
    },
    suggestionsContainerOpen: {
        position: "absolute",
        zIndex: 1,
        left: 0,
        right: 0,
    },
    suggestion: {
        display: "block",
    },
    suggestionsList: {
        margin: 0,
        padding: 0,
        listStyleType: "none",
    },
    divider: {
        height: theme.spacing.unit * 2,
    },
})

export interface ISearchSelectControlProps extends
    IFieldComponentProps,
    IApolloClientProp,
    WithStyles<typeof styles> {
}

export interface ISearchSelectControlState {
    loadState: LoadState
    options: ISelectionOption[]
    value: ISelectionOption | null
    search: string
    suggestions: any[]
    hasMore: boolean
}

export const MAX_RESULTS = 8

export const SearchSelectControl: any = withStyles(styles)(withApolloClient(
    class extends React.Component<ISearchSelectControlProps, ISearchSelectControlState> {
        gridWidthProps: IMUIGridProps
        searchInputRef!: HTMLInputElement

        constructor(props: any) {
            super(props)
            this.gridWidthProps = getGridWidthProps(props)
            this.state = {
                loadState: "not_loaded",
                options: [],
                value: null,
                search: "",
                suggestions: [],
                hasMore: false
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

        handleSuggestionsFetchRequested = (input: any) => {
            const inputValue = input.value.trim().toLowerCase()
            const matches = this.state.options.filter(item => (
                inputValue == "" || item.label.toLowerCase().includes(inputValue)
            ))
            const suggestions = matches.slice(0, MAX_RESULTS)
            const hasMore = matches.length > MAX_RESULTS

            this.setState({ suggestions, hasMore })
        }

        shouldRenderSuggestions = (inputValue: any) => {
            return true
        }

        handleInputClearRequested = () => {
            this.onChange(null)
            setTimeout(this.focusInput, 10)
        }

        handleInputBlur = (event: any, params: BlurEvent<ISelectionOption>) => {
            const value = params.highlightedSuggestion || this.state.value
            this.onChange(value)
        }

        handleSuggestionsClearRequested = () => {
            this.setState({ suggestions: [] })
        }

        handleSearchChange = (event: any, target: any) => {
            this.setState({ search: target.newValue })
        }

        onSuggestionSelected = (event: any, data: SuggestionSelectedEventData<ISelectionOption>) => {
            if (data.suggestion) {
                this.onChange(data.suggestion)
            }
        }

        onChange = (value: ISelectionOption | null) => {
            this.setState({
                value: value ? value : null,
                search: value ? value.label : ""
            })
            this.props.onChange(value ? value.code : null)
        }

        focusInput = () => {
            this.searchInputRef.focus()
        }

        renderInputComponent = (inputProps: any) => {
            const { classes, ref, inputRef = () => null, ...otherProps } = inputProps
            return (
                <Input
                    fullWidth
                    inputRef={(node) => {
                        ref(node)
                        inputRef(node)
                        this.searchInputRef = node
                    }}
                    endAdornment={
                        <InputAdornment position="end">
                            {this.state.value &&
                                <Icon fontSize="small" style={{ cursor: "pointer" }}
                                    onClick={this.handleInputClearRequested}
                                >clear</Icon>}
                            {!this.state.value &&
                                <Icon fontSize="small" style={{ cursor: "pointer" }}
                                    onClick={this.focusInput}
                                >arrow_drop_down</Icon>}
                        </InputAdornment>
                    }
                    {...otherProps}
                    style={{ marginTop: 16 }}
                />
            )
        }

        getSuggestionValue(suggestion: any) {
            return suggestion.label
        }

        renderSuggestion = (suggestion: any, props: any) => {
            return (
                <MenuItem dense selected={props.isHighlighted} component="div">
                    {suggestion.label}
                </MenuItem>
            )
        }

        render() {
            const { classes } = this.props as any
            const fieldId = this.props.field.name
            const hasErrors = this.props.errors.length > 0
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
                            <Autosuggest
                                renderInputComponent={this.renderInputComponent}
                                renderSuggestion={this.renderSuggestion}
                                shouldRenderSuggestions={this.shouldRenderSuggestions}
                                getSuggestionValue={this.getSuggestionValue}
                                inputProps={{
                                    value: this.state.search,
                                    onChange: this.handleSearchChange,
                                    onBlur: this.handleInputBlur
                                }}
                                theme={{
                                    container: classes.container,
                                    suggestionsContainerOpen: classes.suggestionsContainerOpen,
                                    suggestionsList: classes.suggestionsList,
                                    suggestion: classes.suggestion,
                                }}
                                renderSuggestionsContainer={options => (
                                    <Paper {...options.containerProps} square>
                                        {options.children}
                                        {this.state.hasMore && <MenuItem dense>
                                            <em style={{ fontWeight: 300 }}>
                                                More...
                                        </em>
                                        </MenuItem>}
                                    </Paper>
                                )}
                                suggestions={this.state.suggestions}
                                onSuggestionsFetchRequested={this.handleSuggestionsFetchRequested}
                                onSuggestionsClearRequested={this.handleSuggestionsClearRequested}
                                onSuggestionSelected={this.onSuggestionSelected}
                            />}
                        {this.props.readonly &&
                            <ReadOnlyValue>{this.props.value || ""}</ReadOnlyValue>}
                    </FormControl>
                </Grid>
            )
        }
    }))
