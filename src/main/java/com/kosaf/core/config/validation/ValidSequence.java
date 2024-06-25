package com.kosaf.core.config.validation;

import javax.validation.GroupSequence;

@GroupSequence({ValidGroups.First.class, ValidGroups.Second.class, ValidGroups.Third.class,
    ValidGroups.Fourth.class, ValidGroups.Fifth.class, ValidGroups.Sixth.class,
    ValidGroups.Seventh.class, ValidGroups.Eighth.class, ValidGroups.Ninth.class,
    ValidGroups.Tenth.class})
public interface ValidSequence {
}
