using Citylogia.Server.Core.Entityes;
using Libraries.Db.Base;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text;

namespace Core.Entities
{
    [Table("Favorite-Place-Links")]
    public class FavoritePlaceLink : BaseDataType
    {
        [ForeignKey(nameof(User))]
        public long UserId { get; set; }
        public User User { get; set; }

        [ForeignKey(nameof(Place))]
        public long PlaceId { get; set; }
        public Place Place { get; set; }
    }
}
