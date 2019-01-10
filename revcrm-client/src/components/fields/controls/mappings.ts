import { IFieldMetadata } from "../../meta/Metadata"
import { IFieldComponentProps } from "./props"
import { TextControl } from "./TextControl"
import { CheckboxControl } from "./CheckboxControl"
import { SelectControl } from "./SelectControl"

export function getFieldControlMapping(field: IFieldMetadata) {

    const mappings: {
        [fieldType: string]: React.ComponentType<IFieldComponentProps>
    } = {
        IntegerField: TextControl,
        FloatField: TextControl,
        DecimalField: TextControl,
        BooleanField: CheckboxControl,
        TextField: TextControl,
        SelectField: SelectControl,
        DateField: TextControl,
        TimeField: TextControl,
        DateTimeField: TextControl,
        EnumField: TextControl,
        RelatedEntityField: TextControl,
    }

    const control = mappings[field.type]
    if (!control)
        throw new Error(`No control mapping for field type '${field.type}'.`)

    return control
}
