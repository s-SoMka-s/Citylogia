using Libraries.Db.Base;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Photos")]
    public class Photo : BaseDataType
    {
        public Photo()
        {
            this.PublicUrl = string.Empty;
            this.Name = string.Empty;
        }


        public string PublicUrl { get; set; }
        public string Name { get; set; }
    }
}
