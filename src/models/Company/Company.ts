
import * as rev from 'rev-models';

export class Company extends rev.Model {
    @rev.AutoNumberField({ primaryKey: true })
        id: number;
    @rev.TextField()
        name: string;
    @rev.URLField()
        url: string;

    validate(vc: rev.IValidationContext) {

    }

    validateAsync(vc: rev.IValidationContext) {

    }
}
