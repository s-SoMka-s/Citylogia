using Libraries.Db.Base;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Photos")]
    public class Photo : BaseDataType
    {
        public Photo() : base()
        {
            this.Link = string.Empty;
        }


        public string Link { get; set; }
    }
}
