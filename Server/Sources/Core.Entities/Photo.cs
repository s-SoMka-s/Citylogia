using Libraries.Db.Base;
using System.ComponentModel.DataAnnotations.Schema;

namespace Citylogia.Server.Core.Entityes
{
    [Table("Photos")]
    public class Photo : BaseDataType
    {
        public Photo() : base()
        {
            this.PublicUrl = string.Empty;
        }


        public string PublicUrl { get; set; }

        [ForeignKey(nameof(Place))]
        public long PlaceId { get; set; }
        public Place Place { get; set; }
    }
}
