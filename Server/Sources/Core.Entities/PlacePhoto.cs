using Citylogia.Server.Core.Entityes;
using Libraries.Db.Base;
using System.ComponentModel.DataAnnotations.Schema;

namespace Core.Entities
{
    [Table("Place-Photo")]
    public class PlacePhoto : BaseDataType
    {
        public PlacePhoto()
        {
            this.IsMain = false;
        }

        [ForeignKey(nameof(Photo))]
        public long PhotoId { get; set; }
        public Photo Photo { get; set; }

        public bool IsMain { get; set; }

        [ForeignKey(nameof(Place))]
        public long PlaceId { get; set; }
        public Place Place { get; set; }

    }
}
