
import * as rev from 'rev-models';

// User Metadata

export class UserMeta extends rev.Model {

    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.EmailField({ label: 'Email', required: false })
        email: string;
    @rev.BooleanField({ label: 'Active?' })
        active: boolean;
    @rev.DateField({ label: 'Last Login', required: false })
        last_login: Date;

}
