using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace Citylogia.Server.Entityes
{
    public class Place
    {
        public long Id { get; set; }
        public long Mark { get; set; }
        public long TypeId { get; set; }
        public string Name { get; set; }
        public string Description { get; set; }
    }
}
