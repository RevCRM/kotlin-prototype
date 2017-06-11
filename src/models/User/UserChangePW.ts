
import * as rev from 'rev-models';

// User Metadata

export class UserMeta extends rev.Model {

    @rev.TextField({ label: 'Username' })
        username: string;
    @rev.PasswordField({ label: 'New Password' })
        new_password: string;
    @rev.PasswordField({ label: 'Re-type New Password' })
        new_password_repeat: string;

    validate(vc: rev.IValidationContext) {
        if (this.new_password_repeat != this.new_password) {
            vc.result.addFieldError('new_password_repeat', 'The re-typed password does not match!');
        }
    }
}
