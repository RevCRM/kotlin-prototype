import * as React from "react"
import { IGridProps } from "../views/Grid"
import { IMetadataContextProp, withMetadataContext, IFieldMetadata } from "../meta/Metadata"
import { IFormContextProp, withFormContext } from "../views/FormView"
import { IFieldComponentProps, IFieldError, getStandardHTMLProps } from "./controls/props"
import { getFieldControlMapping } from "./controls/mappings"

export interface IFieldProps extends
                    IGridProps,
                    IMetadataContextProp,
                    IFormContextProp {
    name: string
    label?: string
    component?: React.ComponentType<IFieldComponentProps>
}

export interface IFieldState {
    value: any
}

export const Field = withMetadataContext(withFormContext(
    class extends React.Component<IFieldProps, IFieldState> {
    field: IFieldMetadata

    constructor(props: any) {
        super(props)

        const { meta, form } = this.props
        const entity = meta.getEntity(form.entity)!
        this.field = entity.fields.find(f => f.name == this.props.name)!
        if (!this.field)
            throw new Error(`Field '${this.props.name}' not found in data for entity '${entity.name}'`)

        this.state = {
            value: this.props.form.entityData[this.field.name]
        }
    }

    onChange = (value: any) => {
        this.props.form.onFieldChange(this.field, value)
        this.setState({ value })
    }

    render() {

        const { value } = this.state
        const errors: IFieldError[] = []
        const disabled = false

        const componentProps: IFieldComponentProps = {
            field: this.field,
            label: this.props.label || this.field.label,
            colspanNarrow: this.props.colspanNarrow || 12,
            colspan: this.props.colspan || 6,
            colspanWide: this.props.colspanWide || this.props.colspan || 6,
            value,
            errors,
            disabled,
            onChange: this.onChange
        }
        const standardProps = getStandardHTMLProps(this.props)

        const Component = this.props.component || getFieldControlMapping(this.field)
        return <Component {...componentProps} {...standardProps} />
    }
}))
