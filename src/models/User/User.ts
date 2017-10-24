
import * as rev from 'rev-models';

export class User {

    @rev.AutoNumberField()
        id: number;
    @rev.EmailField({ label: 'Email', required: false })
        email: string;
    @rev.BooleanField({ label: 'Active?' })
        active: boolean;

}
