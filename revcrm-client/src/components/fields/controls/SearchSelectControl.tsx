import React from "react"
import Autosuggest from "react-autosuggest"
import match from "autosuggest-highlight/match"
import parse from "autosuggest-highlight/parse"
import Paper from "@material-ui/core/Paper"
import MenuItem from "@material-ui/core/MenuItem"
import { withStyles, Theme, createStyles, WithStyles } from "@material-ui/core/styles"
import { IFieldComponentProps } from "./props"
import { Input, Grid, FormControl, InputLabel } from "@material-ui/core"
import { getGridWidthProps, IMUIGridProps } from "../../views/Grid"

const data = [
    { label: "Afghanistan" },
    { label: "Aland Islands" },
    { label: "Albania" },
    { label: "Algeria" },
    { label: "American Samoa" },
    { label: "Andorra" },
    { label: "Angola" },
    { label: "Anguilla" },
    { label: "Antarctica" },
    { label: "Antigua and Barbuda" },
    { label: "Argentina" },
    { label: "Armenia" },
    { label: "Aruba" },
    { label: "Australia" },
    { label: "Austria" },
    { label: "Azerbaijan" },
    { label: "Bahamas" },
    { label: "Bahrain" },
    { label: "Bangladesh" },
    { label: "Barbados" },
    { label: "Belarus" },
    { label: "Belgium" },
    { label: "Belize" },
    { label: "Benin" },
    { label: "Bermuda" },
    { label: "Bhutan" },
    { label: "Bolivia, Plurinational State of" },
    { label: "Bonaire, Sint Eustatius and Saba" },
    { label: "Bosnia and Herzegovina" },
    { label: "Botswana" },
    { label: "Bouvet Island" },
    { label: "Brazil" },
    { label: "British Indian Ocean Territory" },
    { label: "Brunei Darussalam" },
]

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
                                WithStyles<typeof styles> {
}

export interface ISearchSelectControlState {
    search: string
    suggestions: any[]
    hasMore: boolean
}

export const MAX_RESULTS = 8

export const SearchSelectControl: any = withStyles(styles)(
    class extends React.Component<ISearchSelectControlProps, ISearchSelectControlState> {
    gridWidthProps: IMUIGridProps

    constructor(props: any) {
        super(props)
        this.gridWidthProps = getGridWidthProps(props)
        this.state = {
            search: "",
            suggestions: [],
            hasMore: false
        }
    }

    handleSuggestionsFetchRequested = (input: any) => {
        const inputValue = input.value.trim().toLowerCase()
        const matches = data.filter(item => (
            inputValue == "" || item.label.toLowerCase().includes(inputValue)
        ))
        const suggestions = matches.slice(0, MAX_RESULTS)
        const hasMore = matches.length > MAX_RESULTS

        this.setState({ suggestions, hasMore })
    }

    handleSuggestionsClearRequested = () => {
        this.setState({ suggestions: [] })
    }

    handleChange = (event: any, target: any) => {
        this.setState({ search: target.newValue })
    }

    renderInputComponent = (inputProps: any) => {
        const { classes, ref, inputRef = () => null, ...otherProps } = inputProps
        const fieldId = this.props.field.name
        const hasErrors = this.props.errors.length > 0
        return (
            <FormControl fullWidth>
                <InputLabel
                    htmlFor={fieldId}
                    error={hasErrors}
                    shrink={true}
                >
                    {this.props.label}
                </InputLabel>
                <Input
                    fullWidth
                    inputRef={(node) => {
                        ref(node)
                        inputRef(node)
                    }}
                    {...otherProps}
                />
            </FormControl>
        )
    }

    getSuggestionValue(suggestion: any) {
        return suggestion.label
    }

    shouldRenderSuggestions = (inputValue: any) => {
        return true
    }

    renderSuggestion = (suggestion: any, props: any) => {
        const matches = match(suggestion.label, props.query)
        const parts = parse(suggestion.label, matches)
        return (
            <MenuItem dense selected={props.isHighlighted} component="div">
                <div>
                    {parts.map((part, index) => {
                    return part.highlight ? (
                        <span key={String(index)} style={{ fontWeight: 500 }}>
                        {part.text}
                        </span>
                    ) : (
                        <strong key={String(index)} style={{ fontWeight: 300 }}>
                        {part.text}
                        </strong>
                    )
                    })}
                </div>
            </MenuItem>
        )
    }

    render() {
        const { classes } = this.props as any
        return (
            <Grid item {...this.gridWidthProps} style={this.props.style}>
                <Autosuggest
                    renderInputComponent={this.renderInputComponent}
                    renderSuggestion={this.renderSuggestion}
                    shouldRenderSuggestions={this.shouldRenderSuggestions}
                    getSuggestionValue={this.getSuggestionValue}
                    inputProps={{
                        value: this.state.search,
                        onChange: this.handleChange
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
                                <em style={{ fontWeight: 500 }}>
                                    More...
                                </em>
                            </MenuItem>}
                        </Paper>
                    )}
                    suggestions={this.state.suggestions}
                    onSuggestionsFetchRequested={this.handleSuggestionsFetchRequested}
                    onSuggestionsClearRequested={this.handleSuggestionsClearRequested}
                />
            </Grid>
        )
    }
})
