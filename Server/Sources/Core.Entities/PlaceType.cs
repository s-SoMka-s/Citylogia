using Libraries.Db.Base;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Places-Types")]
    public class PlaceType : BaseDataType
    {
        public PlaceType()
        {
            this.Name = string.Empty;
        }


        public string Name { get; set; }
    }
}
