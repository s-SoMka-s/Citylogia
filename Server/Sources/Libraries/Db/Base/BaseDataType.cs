﻿using System.ComponentModel.DataAnnotations;

namespace Libraries.Db.Base
{
    public class BaseDataType : IDataType
    {
        public BaseDataType()
        {
            this.Deleted = false;
        }

        [Key]
        public long Id { get; set; }
        public bool Deleted { get; set; }
    }
}
