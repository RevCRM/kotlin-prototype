import * as React from "react"
import { IGridProps } from "../views/Grid"
import { IMetadataContextProp, withMetadataContext } from "../meta/Metadata"
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

export const Field = withMetadataContext(withFormContext(
    class extends React.Component<IFieldProps> {

    render() {
        // move to constructor. dont assume entity or field is found
        const { meta, form } = this.props
        const entity = meta.getEntity(form.entity)!
        const field = entity.fields.find(f => f.name == this.props.name)

        if (!field) throw new Error(`Field '${this.props.name}' not found in data for entity '${entity.name}'`)

        const value = form.entityData[field.name]
        const errors: IFieldError[] = []
        const disabled = false

        const componentProps: IFieldComponentProps = {
            field,
            label: this.props.label || field.label,
            colspanNarrow: this.props.colspanNarrow || 12,
            colspan: this.props.colspan || 6,
            colspanWide: this.props.colspanWide || this.props.colspan || 6,
            value,
            errors,
            disabled,
            onChange: (newValue) => null
        }
        const standardProps = getStandardHTMLProps(this.props)

        const Component = this.props.component || getFieldControlMapping(field)
        return <Component {...componentProps} {...standardProps} />
    }
}))
