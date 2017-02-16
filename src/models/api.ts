
import * as api from 'rev-api';

import User from './User/User';

api.register(User, { operations: 'all' });
